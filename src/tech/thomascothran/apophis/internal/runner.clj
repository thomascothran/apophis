(ns tech.thomascothran.apophis.internal.runner
  (:require [tech.thomascothran.apophis.ports :as p]))

(defn fn-survivors
  [test-fn testable-fn mutations]
  (let [original-form (get testable-fn :form)
        form-ns       (get testable-fn :namespace)
        fn-symbol     (get testable-fn :symbol)
        mutations     (p/spawn-mutants mutations original-form)]
    (mapv (fn [mutation]
            (binding [*ns* form-ns] (eval mutation)
                     (try (assoc (test-fn)
                                 :mutation mutation
                                 :ns (symbol (str form-ns))
                                 :symbol fn-symbol
                                 :original-form original-form)
                          (catch Exception e
                            {:exception e
                             :mutation mutation}))))
          mutations)))

(defn run-fn-all
  [test-fn testable-fn mutations]
  (let [form-ns (get testable-fn :namespace)
        ns-sym  (symbol (str form-ns))
        _       (require ns-sym)
        result  (fn-survivors test-fn testable-fn mutations)]
    (require ns-sym :reload-all)
    result))
