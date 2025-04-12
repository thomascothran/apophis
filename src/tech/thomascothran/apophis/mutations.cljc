(ns tech.thomascothran.apophis.mutations)

(def default-swaps
  {`if #{`if-not}
   `if-not #{`if}
   `and  #{`or}
   `when #{`when-not}
   `when-not #{`when}
   `filter #{`remove}
   `empty? #{`seq `not-empy?}
   `seq #{`empty?}
   `some? #{`any?}
   `any? #{`some}
   `boolean #{`not}
   `< #{`= `>}
   `+ #{`-}
   `* #{`/}
   `= #{`not=}})
