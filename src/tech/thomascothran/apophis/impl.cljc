(ns tech.thomascothran.apophis.impl
  (:require [clojure.zip :as z]
            [clojure.java.io :as io]
            [edamame.core :as e]
            [tech.thomascothran.apophis.internal.runner :as r]
            [tech.thomascothran.apophis.ports :as ports]))

(defn spawn-mutants
  [mutations form]
  (loop [mutants #{}
         loc (z/seq-zip form)]
    (let [mutants' (->> (mutations (z/node loc))
                        (map #(z/root (z/replace loc %)))
                        (into mutants))]
      (if (z/end? loc)
        mutants'
        (recur mutants' (z/next loc))))))

(defmethod ports/spawn-mutants
  :default
  [mutations forms]
  (spawn-mutants mutations forms))

(defn fn-sym->testable
  "For a given function var, find its source"
  [fn-sym]
  (let [fn-var (find-var fn-sym)

        {:keys [file line]} (meta fn-var)

        fn-ns (find-ns (symbol (namespace (symbol fn-var))))

        file-contents (slurp (io/resource file))

        ns-forms (e/parse-string-all
                  file-contents
                  {:auto-resolve-ns true
                   :auto-resolve {:current
                                  (symbol (str fn-ns)) ; resolve the current namespace
                                  :all true} ; resolve all symbols
                   :syntax-quote true})

        form
        (->> ns-forms
             (filter (comp (partial = line) :row meta))
             first)]
    ^{:type :fn}
    {:namespace fn-ns
     :symbol fn-sym
     :form form}))

(defmethod ports/find-testable
  [:fn #?(:clj clojure.lang.Symbol
          :cljs Symbol)]
  [args]
  (fn-sym->testable (second args)))

(comment
  (ports/find-testable
   [:fn 'tech.thomascothran.apophis.impl-test.testns/testable-fn]))

(defn- failed?
  [result]
  (pos? (get result :fail)))

(defmethod ports/survivors
  [:all :fn]
  [_strategy test-fn testable mutations]
  ;; you have to find the testable here!
  (filter failed?
          (r/run-fn-all test-fn testable mutations)))
