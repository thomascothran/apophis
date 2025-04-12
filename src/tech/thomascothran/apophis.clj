(ns tech.thomascothran.apophis
  (:require [tech.thomascothran.apophis.ports :as ports]
            [tech.thomascothran.apophis.mutations :as mutations]
            [tech.thomascothran.apophis.impl]))

(defn survivors
  "Returns a sequence of survivors.

  Params
  ------
  - `test-fn` is your test function that will run the test suite.
     It must return a map with the key `:fail`, with an integer
     value representing the number of failed tests.
  - `testable` is anything that we can run tests agains. For
     example, `[:fn #'my-app/my-fn]`"
  [test-fn testable]
  (ports/survivors :all test-fn
                   (ports/find-testable testable)
                   mutations/default-swaps))

(comment
  ;; ...
  (survivors (constantly {:fail 0 :pass 1 :test 1})
             [:fn 'tech.thomascothran.apophis.impl-test.testns/testable-fn]))
