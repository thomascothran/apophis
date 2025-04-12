(ns tech.thomascothran.apophis.impl-test.testns
  "this ns exists only to be looked up by the tests"
  (:require [clojure.set :as set]))

(def ^:private private-map
  {:public false})

(def public-map
  {:public true})

(defn- private-fn
  [a] (not a))

(defn testable-fn
  [a b]
  (if a public-map b))

(defn another-testable-fn
  [a]
  (constantly a))

(defn fn-ns-kw
  [a]
  [::a ::set/a a])

(comment
  (->> (ns-interns *ns*)
       vals
       (map symbol)))
