(ns tech.thomascothran.apophis.internal.runner-test
  (:require [clojure.test :refer [deftest is]]
            [tech.thomascothran.apophis.internal.runner :as r]
            [tech.thomascothran.apophis.impl-test.testns :as tns]))

(def if-mutations
  {`if #{`if-not}})

(def testable-fn
  {:form '(defn testable-fn [a b]
            (if a public-map b))
   :namespace
   (find-ns 'tech.thomascothran.apophis.impl-test.testns)})

(defn test-fn
  []
  (let [result (tns/testable-fn true :expected)]
    (if (= :expected result)
      {:test 1 :pass 1 :fail 0}
      {:test 1 :pass 0 :fail 1})))

(deftest test-return-results
  (is (= [{:fail 0,
           :mutation '(defn testable-fn [a b] (clojure.core/if-not a public-map b)),
           :pass 1
           :test 1}]
         (r/run-fn-all test-fn
                       testable-fn
                       if-mutations))))
