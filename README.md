# Apophis

Mutation inspired testing tools for Clojure.

## Design Goals

### Choose what you want to test

Choose whether to test an individual function, namespace, or project.

### Assess quality of code base

Use mutation tests to check the quality of code bases at the namespace level.

### Pluggable

Extend apophis to test pull requests or commits. Plug in different mutations at your choosing.

## API

```clojure
(ns my.test.ns
  (:require [clojure.test :refer [deftest is run-all-tests]]
            [tech.thomascothran.apophis :as a]
            [my.test.stuff :as stuff])

(deftest test-my-fn
  (is (empty? (a/survivors run-all-tests
                           [:fn #'stuff/my-fn]))
      "Should be no survivors"))
```

## License

MIT
