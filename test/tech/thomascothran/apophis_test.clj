(ns tech.thomascothran.apophis-test
  (:require [clojure.test :refer [deftest is]]
            [tech.thomascothran.apophis :as a]))

(deftest test-returns-nothing-when-successful
  (is (empty? (a/survivors (constantly {:fail 0 :pass 1 :test 1})
                           [:fn 'tech.thomascothran.apophis.impl-test.testns/testable-fn]))))

(deftest test-returns-survivors
  (let [result
        (a/survivors (constantly {:fail 1 :pass 1 :test 2})
                     [:fn 'tech.thomascothran.apophis.impl-test.testns/testable-fn])]
    (is (= 1 (count result)))
    (is (= '(defn testable-fn [a b] (clojure.core/if-not a public-map b))
           (-> result
               first
               (get :mutation))))))
