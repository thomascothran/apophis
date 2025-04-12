(ns tech.thomascothran.apophis.impl-test
  (:require [clojure.test :refer :all]
            [tech.thomascothran.apophis.impl-test.testns :as tns]
            [tech.thomascothran.apophis.impl
             :refer [spawn-mutants fn-sym->testable]]))

(deftest test-spawn-mutants
  (testing "Given a simple if statement
    And a mutation that turns if to if-not
    Then there should be one mutant
    And the if should be an if-not "
    (let [simple-if `(if a :a :b)
          if-mutation {`if #{`if-not}}
          expected #{`(if-not a :a :b)}
          result (spawn-mutants if-mutation simple-if)]
      (is (= expected result))))

  (testing "Given a nested if statement
    And a mutation that turns an if to an if-not
    Then there should be a mutation where the if is an if not"
    (let [nested-if `(defn f [a]
                       (if a :a :b))
          if-mutation {`if #{`if-not}}
          expected #{`(defn f [a]
                        (if-not a :a :b))}
          result (spawn-mutants if-mutation nested-if)]
      (is (= expected result))))

  (testing "Given a < comparator
    And mutations for the < comparator including > and =
    Then there should be a mutation for both > and ="
    (let [<form `(< 1 2)
          <mutation {`< #{`> `=}}
          expected #{`(> 1 2) `(= 1 2)}
          result (spawn-mutants <mutation <form)]
      (is (= expected result)))))

(deftest test-find-src-form
  (require 'tech.thomascothran.apophis.impl-test.testns :reload)
  (is (= {:form '(defn testable-fn [a b]
                   (if a public-map b))
          :symbol 'tech.thomascothran.apophis.impl-test.testns/testable-fn
          :namespace
          (find-ns 'tech.thomascothran.apophis.impl-test.testns)}

         (fn-sym->testable 'tech.thomascothran.apophis.impl-test.testns/testable-fn))))
(comment
  (meta #'tech.thomascothran.apophis.impl-test.testns/testable-fn)
  (name (symbol #'tns/testable-fn)))

(deftest test-find-src-form-ns-resolution
  (is (= {:form '(defn fn-ns-kw
                   [a]
                   [:tech.thomascothran.apophis.impl-test.testns/a
                    :clojure.set/a
                    a])

          :symbol 'tech.thomascothran.apophis.impl-test.testns/fn-ns-kw
          :namespace
          (find-ns 'tech.thomascothran.apophis.impl-test.testns)}

         (fn-sym->testable 'tech.thomascothran.apophis.impl-test.testns/fn-ns-kw))))

#_(defn find-testables
    ([paths] (find-testables paths (all-ns)))
    ([paths namespaces]
     (let [paths' (mapv str paths)
           filterer
           (fn [ns']
             (some #(str/starts-with? (str ns') %)
                   paths'))]
       (filter filterer namespaces))))

#_(deftest test-find-testables
    (testing "Given that I have a path of what should be tested
    When I try to find testables
    Then it returns the source for the functions in that ns"
      (let [paths #{"tech.thomascothran.apophis.impl-test."}]
        (is (= [(find-ns 'tech.thomascothran.apophis.impl-test.testns)]
               (find-testables paths))))))
