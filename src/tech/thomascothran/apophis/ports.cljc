(ns tech.thomascothran.apophis.ports)

(defmulti spawn-mutants
  (fn [mutations _form]
    (type mutations)))

(defmulti find-testable
  (fn [reference]
    (type reference)))

(defmulti survivors
  (fn [strategy _test-fn testable _mutations]
    [strategy (type testable)]))
