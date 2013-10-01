;;
;; 4clojure.com Solutions, 2013 Paul Bowler
;; Distributed under the Eclipse Public License, the same as 4Clojure.
;;

(ns fourclojure.core)

; 1. Simple Maths

(= true true)
(= (not false) true)

; 2. Simple Maths

(= (- 10 (* 2 3)) 4)

; 3. Intro to Strings

(= "HELLO WORLD" (.toUpperCase "hello world"))

; More idiomatic to use clojure upper-string function rather than java interop

(= "HELLO WORLD" (clojure.string/upper-case "hello world"))

; Resulting data are identical

(= (clojure.string/upper-case "hello world") (.toUpperCase "hello world"))

; Not surprising as upper-case uses Java under the hood, and Java strings are immutable:

(defn ^String upper-case
  "Converts string to all upper-case."
  {:added "1.2"}
  [^CharSequence s]
  (.. s toString toUpperCase))

; 4. Intro to Lists

(= (list :a :b :c) '(:a :b :c))

; 5. Lists: conj

(= (list 1 2 3 4) (conj '(2 3 4) 1))
(= (list 1 2 3 4) (conj '(3 4) 2 1))

; or using a quoted form:

(= '(1 2 3 4) (conj '(2 3 4) 1))
(= '(1 2 3 4) (conj '(3 4) 2 1))

; 6. Intro to Vectors - all resulting data structures are logically equal even though they may be different collection types

(= [:a :b :c] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))

; 7. Vectors: conj

(= [1 2 3 4] (conj [1 2 3] 4))
(= [1 2 3 4] (conj [1 2] 3 4))

; or we could use the vector function:

(= (vector 1 2 3 4) (conj [1 2 3] 4))
(= (vector 1 2 3 4) (conj [1 2] 3 4))

; or the vec function with a list:

(= (vec '(1 2 3 4)) (conj [1 2 3] 4))
(= (vec '(1 2 3 4)) (conj [1 2] 3 4))

; or simply a list:

(= '(1 2 3 4) (conj [1 2 3] 4))
(= '(1 2 3 4) (conj [1 2] 3 4))

; 8. Intro to Sets - Sets only hold unique values, not duplicates. Sets use the #{} format.

(= #{:a :b :c :d} (set '(:a :a :b :c :c :c :c :d :d)))
(= #{:a :b :c :d} (clojure.set/union #{:a :b :c} #{:b :c :d}))

; Sets can also be created with the set function taking either a (quoted) list or vector

(= (set [:a :b :c :d]) (set '(:a :a :b :c :c :c :c :d :d)))
(= (set [:a :b :c :d]) (clojure.set/union #{:a :b :c} #{:b :c :d}))

; 9. Sets: conj - sets are naturally ordered using the underlying data as the comparator

(= #{1 2 3 4} (conj #{1 4 3} 2))

; 10. Intro to Maps - keywords can be used as lookup functions

(= 20 ((hash-map :a 10, :b 20, :c 30) :b))
(= 20 (:b {:a 10, :b 20, :c 30}))

; 11. Maps: conj - You can conjoin either a vector or a map, but not (oddly) a list - this gives a Class Cast Exception

(= {:a 1, :b 2, :c 3} (conj {:a 1} {:b 2} [:c 3]))
(= {:a 1, :b 2, :c 3} (conj {:a 1} [:b 2] [:c 3]))
; (= {:a 1, :b 2, :c 3} (conj {:a 1} '(:b 2) [:c 3])) - java.lang.ClassCastException

; 12. Intro to Sequences

(= 3 (first '(3 2 1)))
(= 3 (second [2 3 4]))
(= 3 (last (list 1 2 3)))

; 13. Sequences: rest - remember the equivalence from Q6

(= [20 30 40] (rest [10 20 30 40]))
(= (vector 20 30 40) (rest [10 20 30 40]))
(= '(20 30 40) (rest [10 20 30 40]))

; 14. Intro to Functions

(= 8 ((fn add-five [x] (+ x 5)) 3))
(= 8 ((fn [x] (+ x 5)) 3))
(= 8 (#(+ % 5) 3))
(= 8 ((partial + 5) 3))

; 15. Double Down - many alternatives

(= ((fn double-down [x] (* 2 x)) 2) 4)
(= ((fn [x] (+ x x) 3) 6)
(= (#(* 2 %1) 11) 22)
(= (#(* 2 %) 7) 14)

; 16 Hello World

(= ((fn hello-world [name] (str "Hello, " name "!")) "Dave") "Hello, Dave!")
(= (#(str "Hello, " % "!") "Jenn") "Hello, Jenn!")
(= (#(str "Hello, " % "!") "Rhea") "Hello, Rhea!")

; 17. Sequences: map - remember equivalences from Q6

(= [6 7 8] (map #(+ % 5) '(1 2 3)))
(= '(6 7 8) (map #(+ % 5) '(1 2 3)))
(= (vector 6 7 8) (map #(+ % 5) '(1 2 3)))
(= (list 6 7 8) (map #(+ % 5) '(1 2 3)))

; 18. Sequences: filter

(= '(6 7) (filter #(> % 5) '(3 4 5 6 7)))

; 19. Last Element - not allowed to use the 'last' function, essentially writing our own

; Options:
; 	We can use 'nth' along with 'count' minus 1
;	We could also use 'dec' instead of minus to remove a magic number
;	Or we could reverse the list and take the first item - reverse is not lazy though
;	Or using 'reduce' with a bare function

(= (#(nth % (- (count %) 1)) [1 2 3 4 5]) 5)
(= (#(nth % (dec (count %))) '(5 4 3)) 3)
(= (#(first (reverse %)) ["b" "c" "d"]) "d")
(= (reduce (fn [a b] b) ["b" "c" "d"]) "d")

; Are there efficiency tradeoffs for each of these options?
; Using a larger data set of 1,000,000 entries we get the following:

(time (#(nth % (- (count %) 1)) [range 1000000]))
"Elapsed time: 0.119 msecs"

(time (#(first (reverse %)) [range 1000000]))
"Elapsed time: 0.107 msecs"

(time (reduce (fn [x y] y) [range 1000000]))
"Elapsed time: 0.116 msecs"

; With this example the timings are roughly equivalent

; 20. Penultimate Element

; Options:
;	We can reverse the list, drop the head item and return the next
; 	We can take the 'nth' using the count - 2
;	Or drop the first 'count minus 2' and take the next)

(= (#(first (drop 1 (reverse %))) (list 1 2 3 4 5)) 4)
(= (#(nth % (- (count %) 2)) ["a" "b" "c"]) "b")
(= (#(first (drop (- (count %) 2) %)) [[1 2] [3 4]]) [1 2])

; 21. Nth Element - without using 'nth'

; Options:
;	Drop the first 'n' items and then return the first

(= (#(first (drop %2 %1)) '(4 5 6 7) 2) 6)
(= (#(first (drop %2 %1)) [:a :b :c] 0) :a)
(= (#(first (drop %2 %1)) [1 2 3 4] 1) 2)
(= (#(first (drop %2 %1)) '([1 2] [3 4] [5 6]) 2) [5 6])

; 22. Count a Sequence - without using count

; Options:
;	Iterate through each item adding 1 to a counter. Reduce is great for this type of thing.
;	Add together a sequence of 1's using Map - two options here; use 'constantly or a new function

(= (#(reduce (fn [a b] (inc a)) 0 %) '(1 2 3 3 1)) 5)
(= (#(reduce (fn [a b] (inc a)) 0 %) "Hello World") 11)
(= (#(reduce + (map (constantly 1) %)) [[1 2] [3 4] [5 6]]) 3)
(= (fn [coll] (apply + (map (fn [x] 1) coll)) '(13)) 1)
(= (#(apply + (map (fn [x] 1) %)) '(:a :b :c)) 3)

; 23. Reverse a Sequence - without using reverse or rseq

; Options:
;	Take the nth using count and repeat using dec until zero
;	Use a filo queue-like structure repeatedly popping the top item onto the top of a new list. 'into' does this for us already.

(= (#(into () %) [1 2 3 4 5]) [5 4 3 2 1])
(= (#(into () %) (sorted-set 5 7 2 7)) '(7 5 2))
(= (#(into () %) [[1 2][3 4][5 6]]) [[5 6][3 4][1 2]])

; 24. Sum It All Up - Exactly what 'reduce' was made for

(= (#(reduce + %) [1 2 3]) 6)
(= (#(reduce + %) (list 0 -2 5 5)) 8)
(= (#(reduce + %)) 7)
(= (#(reduce + %) '(0 0 -1)) -1)
(= (#(reduce + %) '(1 10 3)) 14)

; 25. Find the odd numbers - perfect candidate for the 'filter' and 'odd?' functions

(= (#(filter odd? %) #{1 2 3 4 5}) '(1 3 5))
(= (#(filter odd? %) [4 2 1 6]) '(1))
(= (#(filter odd? %) [2 2 4 6]) '())
(= (#(filter odd? %) [1 1 1 3]) '(1 1 1 3))

; 26. Fibonacci Sequence - typically a good candidate for recursion (from my ML days) or an infinite lazy sequence with 'take'

; Options:
;	Use stack recursion with base cases. This is fine, but will hit stack limits for large values. Avoid stack-consuming recursion.
;	Use tail recursion with base case for zero. Again, will fail for large values as JVM does not (yet) support tail-call optimisation.
;	Use 'recur' to forse explicit tail-call recursion. This avoid the stack space issues.
;	Use an infinite lazy sequence and then 'take' the first n as required. This will also return a list, as required, and not just the value.
;	Create infinite lazy fibonacci pairs using 'iterate' and then take the first item from each pair using map - my preferred option

(= (#(take % (map first (iterate (fn [[a b]] [b (+ a b)]) [1 1]))) 3) '(1 1 2))
(= (#(take % (map first (iterate (fn [[a b]] [b (+ a b)]) [1 1]))) 6) '(1 1 2 3 5 8))
(= (#(take % (map first (iterate (fn [[a b]] [b (+ a b)]) [1 1]))) 8) '(1 1 2 3 5 8 13 21))

; 27. Palindrome Detector

; Should be able to simply reverse the input and test with the original, however this fails for strings.
; i.e. #(= % (reverse %)) fails for 'racecar' as (reverse "racecar") gives (\r \a \c \e \c \a \r)
; Instead, we need to convert the input into a sequence, reverse it and compare with a sequence of the original

(false? (#(= (seq %) (reverse (seq %))) '(1 2 3 4 5)))
(true? (#(= (seq %) (reverse (seq %))) "racecar"))
(true? (#(= (seq %) (reverse (seq %))) [:foo :bar :foo]))
(true? (#(= (seq %) (reverse (seq %))) '(1 1 3 3 1 1)))
(false? (#(= (seq %) (reverse (seq %))) '(:a :b :c)))

; 28. Flatten a Sequence - without using 'flatten'.

; Options:
;	Could use 'sequential?' to see if item implements Sequential and then iterate (not to be confused with 'seq?' for ISeq's)
;	The core implementation of flatten uses tree-seq to walk the structure as a tree and filter out only the leaves. Nice!

(defn my_flatten [item]
   (if (sequential? item)
     (mapcat my_flatten item)
     (list item)))

(= (my_flatten '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6))
(= (my_flatten ["a" ["b"] "c"]) '("a" "b" "c"))
(= (my_flatten '((((:a))))) '(:a))

; 29. Get the Caps

; Use a regex to extract the caps into a sequence then str them together.
; Note that str on its own will str the whole string as it is. Instead you must 'apply' str to each item in the sequence.

(= (#(apply str (re-seq #"[A-Z]" %)) "HeLlO, WoRlD!") "HLOWRD")
(empty? (#(apply str (re-seq #"[A-Z]" %)) "nothing"))
(= (#(apply str (re-seq #"[A-Z]" %)) "$#A(*&987Zf") "AZ")

; 30. Compress a Sequence

; We can use 'partition-by' to split the sequence when the item changes. We then need to take the first item
; from each sub-sequence using 'map' and 'first'. Job done!

(= (apply str (#(map first (partition-by identity %)) "Leeeeeerrroyyy")) "Leroy")
(= (#(map first (partition-by identity %)) [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))
(= (#(map first (partition-by identity %)) [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))

; 31. Pack a Sequence - Simply use the same method as 30 using 'partition-by'.

(= (#(partition-by identity %) [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))
(= (#(partition-by identity %) [:a :a :b :b :c]) '((:a :a) (:b :b) (:c)))
(= (#(partition-by identity %) [[1 2] [1 2] [3 4]]) '(([1 2] [1 2]) ([3 4])))

; 32. Duplicate a Sequence

; We can duplicate the sequence and then interleave elements from each resulting sequence in turn

(= (#(apply interleave (repeat 2 %)) [1 2 3]) '(1 1 2 2 3 3))
(= (#(apply interleave (repeat 2 %)) [:a :a :b :b]) '(:a :a :a :a :b :b :b :b))
(= (#(apply interleave (repeat 2 %)) [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))
(= (#(apply interleave (repeat 2 %)) [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))

; 33. Replicate a Sequence

; On the face of it is the same as 32 but with a variable repetition.
; i.e. #(apply interleave (repeat %2 %1))
; However, interleave does not work with a single arity (i.e. one input sequence) in versions of clojure before 1.6.
; For that we need to wrap in another function that uses mapcat on that function to repeat n times

(= (#(apply interleave (repeat %2 %1)) [1 2 3] 2) '(1 1 2 2 3 3))
(= (#(apply interleave (repeat %2 %1)) [:a :b] 4) '(:a :a :a :a :b :b :b :b))
(= ((fn replicate-seq [coll n] (mapcat #(repeat n %) coll)) [4 5 6] 1) '(4 5 6))
(= ((fn replicate-seq [coll n] (mapcat #(repeat n %) coll)) [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4]))
(= ((fn replicate-seq [coll n] (mapcat #(repeat n %) coll)) [44 33] 2) [44 44 33 33])

; 34. Implement range - without using range!

; Create a lazy infinite list of increasing integers starting at n1 and take (n2 - n1) of them

(= (#(take (- %2 %1) (iterate inc %1)) 1 4) '(1 2 3))
(= (#(take (- %2 %1) (iterate inc %1)) -2 2) '(-2 -1 0 1))
(= (#(take (- %2 %1) (iterate inc %1)) 5 8) '(5 6 7))

; 35. Local bindings

(= 7 (let [x 5] (+ 2 x)))
(= 7 (let [x 3, y 10] (- y x)))
(= 7 (let [x 21] (let [y 3] (/ x y))))

; 36. Let it Be - Commas are optional, but help clarity

(= 10 (let [x 7, y 3, z 1] (+ x y)))
(= 4 (let [x 7, y 3, z 1] (+ y z)))
(= 1 (let [x 7, y 3, z 1] z))

; 37. Regular Expressions - extracts capital letters from input string

(= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))

; 38. Maximum value - without using max or max-key

; Sort the parameters and take the last (or first if sorting by > function).
; NOTE: Variable arity equivalent of '& more' in anonymous functions is %&

(= ((fn [& more] (last (sort more))) 1 8 3 4) 8)
(= (#(last (sort %&)) 30 20) 30)
(= (#(first (sort > %&)) 45 67 11) 67)

; 39. Interleave Two Seqs - without using interleave

; Use map with a function that simply states the identities of the items then concat together
; Actually, mapcat can do both operations in one go, and the vector function is ideal for identity
; across more than one parameter (identity function itself needs single arity).

(= (#(mapcat vector %1 %2) [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c))
(= (#(mapcat vector %1 %2) [1 2] [3 4 5 6]) '(1 3 2 4))
(= (#(mapcat vector %1 %2) [1 2 3 4] [5]) [1 5])
(= (#(mapcat vector %1 %2) [30 20] [25 15]) [30 25 20 15])

; 40. Interpose a Seq - without using interpose

; Similar to above, but using a single, cycling vector of the interpose item, then drop the last item.

(= (#(drop-last (mapcat vector %2 (cycle [%1]))) 0 [1 2 3]) [1 0 2 0 3])
(= (apply str (#(drop-last (mapcat vector %2 (cycle [%1]))) ", " ["one" "two" "three"])) "one, two, three")
(= (#(drop-last (mapcat vector %2 (cycle [%1]))) :z [:a :b :c :d]) [:a :z :b :z :c :z :d])

; 41. Drop Every Nth Item

; Partition into groups of n length, remove the nth item from each group (if it exists) and then flatten.

(= (#(flatten (map (fn [seq] (take (dec %2) seq)) (partition-all %2 %1))) [1 2 3 4 5 6 7 8] 3) [1 2 4 5 7 8])
(= (#(flatten (map (fn [seq] (take (dec %2) seq)) (partition-all %2 %1))) [:a :b :c :d :e :f] 2) [:a :c :e])
(= (#(flatten (map (fn [seq] (take (dec %2) seq)) (partition-all %2 %1))) [1 2 3 4 5 6] 4) [1 2 3 5 6])

; 42. Factorial Fun

; Create a range from 1 to n then reduce with multiply.
; NOTE: Range by default starts from zero so we need to set the first item and increase the end number by 1.

(= (#(reduce * (range 1 (inc %1))) 1) 1)
(= (#(reduce * (range 1 (inc %1))) 3) 6)
(= (#(reduce * (range 1 (inc %1))) 5) 120)
(= (#(reduce * (range 1 (inc %1))) 8) 40320)

; 43. Reverse Interleave

; Partition in sets of n, then create a new list each item at the same position of each subseq.

(= (#(apply map list (partition %2 %1))) [1 2 3 4 5 6] 2) '((1 3 5) (2 4 6)))
(= (#(apply map list (partition %2 %1))) (range 9) 3) '((0 3 6) (1 4 7) (2 5 8)))
(= (#(apply map list (partition %2 %1))) (range 10) 5) '((0 5) (1 6) (2 7) (3 8) (4 9)))

; 44. Rotate Sequence

; Create an infinite lazy sequence of the list using cycle, drop the modulus of n and length to set yourself
; up on the correct entry, then take the required number of items (same count as original)

(= (#(take (count %2) (drop (mod %1 (count %2)) (cycle %2))) 2 [1 2 3 4 5]) '(3 4 5 1 2))
(= (#(take (count %2) (drop (mod %1 (count %2)) (cycle %2))) -2 [1 2 3 4 5]) '(4 5 1 2 3))
(= (#(take (count %2) (drop (mod %1 (count %2)) (cycle %2))) 6 [1 2 3 4 5]) '(2 3 4 5 1))
(= (#(take (count %2) (drop (mod %1 (count %2)) (cycle %2))) 1 '(:a :b :c)) '(:b :c :a))
(= (#(take (count %2) (drop (mod %1 (count %2)) (cycle %2))) -4 '(:a :b :c)) '(:c :a :b))

; 45. Intro to Iterate

(= [1 4 7 10 13] (take 5 (iterate #(+ 3 %) 1)))

; 46. Flipping out

; This must return a function that returns the params in the reverse order

(= 3 (((fn [func] (fn [x y] (func y x))) nth) 2 [1 2 3 4 5]))
(= true (((fn [func] (fn [x y] (func y x))) >) 7 8))
(= 4 (((fn [func] (fn [x y] (func y x))) quot) 2 8))
(= [1 2 3] ((__ take) [1 2 3 4 5] 3))

; 47. Contain Yourself - Note: Keys in vectors are array positions!

(contains? #{4 5 6} 4)
(contains? [1 1 1 1 1] 4)
(contains? {4 :a 2 :b} 4)
(not (contains? '(1 2 4) 4))

; 48. Intro to some

(= 6 (some #{2 7 6} [5 6 7 8]))
(= 6 (some #(when (even? %) %) [5 6 7 8]))

; 49. Split a sequence - without using split-at

; Take and Drop should do here - with some vec and vector magic

(= (#(vector (vec (take %1 %2)) (vec (drop %1 %2))) 3 [1 2 3 4 5 6]) [[1 2 3] [4 5 6]])
(= (#(vector (vec (take %1 %2)) (vec (drop %1 %2))) 1 [:a :b :c :d]) [[:a] [:b :c :d]])
(= (__ 2 [[1 2] [3 4] [5 6]]) [[[1 2] [3 4]] [[5 6]]])

; 50. Split by Type

; group-by returns a map of types and values, i.e. {java.lang.Long [1 2 3], clojure.lang.Keyword [:a :b :c]}
; We then just need to get the values (ignoring the keys) using vals

(= (set (#(vals (group-by type %)) [1 :a 2 :b 3 :c])) #{[1 2 3] [:a :b :c]})
(= (set (#(vals (group-by type %)) [:a "foo"  "bar" :b])) #{[:a :b] ["foo" "bar"]})
(= (set (#(vals (group-by type %)) [[1 2] :a [3 4] 5 6 :b])) #{[[1 2] [3 4]] [:a :b] [5 6]})

; 51. Advanced Destructuring

(= [1 2 [3 4 5] [1 2 3 4 5]] (let [[a b & c :as d] [1 2 3 4 5]] [a b c d]))

; 52. Intro to Destructuring

(= [2 4] (let [[a b c d e f g] (range)] [c e]))

; 53. Longest Increasing Sub-Seq

; We can partition the sequence using each item with its next neighbour - (partition 2 1 seq]
; Sequences have the second item 1 great than the first, so we next filter just those that conform
; Then flatten the sequence an remove the duplicates introduced in the first step.

(defn longest-seq [coll]
  (->> (partition 2 1 coll)
    (partition-by #(- (second %) (first %)))
    (filter #(= 1 (- (second (first %)) (ffirst %))))
    (reduce #(if (< (count %1) (count %2)) %2 %1) [])
    flatten
    distinct))

(= (longest-seq [1 0 1 2 3 0 4 5]) [0 1 2 3])
(= (longest-seq [5 6 1 3 2 7]) [5 6])
(= (longest-seq [2 3 3 4 5]) [3 4 5])
(= (longest-seq [7 6 5 4]) [])

; 54. Partition a sequence - without using partition functions

; Fairly straight-forward implementation using take and drop with recursion.

(defn my_partition [n seq] (when (<= n (count seq)) (cons (take n seq) (my_partition n (drop n seq)))))

(= (my_partition 3 (range 9)) '((0 1 2) (3 4 5) (6 7 8)))
(= (my_partition 2 (range 8)) '((0 1) (2 3) (4 5) (6 7)))
(= (my_partition 3 (range 8)) '((0 1 2) (3 4 5)))

; 55. Count Occurrences - without using frequencies

; Sort the items then partition by identity to give ((1 1 1 1) (2 2) (3))
; The map using a new function that returns a map of the identity with the count to give ({1 4} {2 2} {3 1})
; Then flatten the structure into a single map using 'into'.
; Could also have used juxt - see Q59

(= (#(into {} (map (fn [seq] {(first seq) (count seq)}) (partition-by identity (sort %)))) [1 1 2 3 2 1 1]) {1 4, 2 2, 3 1})
(= (#(into {} (map (fn [seq] {(first seq) (count seq)}) (partition-by identity (sort %)))) [:b :a :b :a :b]) {:a 2, :b 3})
(= (#(into {} (map (fn [seq] {(first seq) (count seq)}) (partition-by identity (sort %)))) '([1 2] [1 3] [1 3])) {[1 2] 1, [1 3] 2})

; 56. Find Distinct Items - without using distinct

; Options:
;	My first thought was to use a set to remove duplicates: #(into [] (into #{} %))
;   However, this does not maintain the order and fails the last 3 tests.
;	Using a sorted set passes the first 2 tests, but fails the last 2: #(into [] (apply sorted-set %))
;	Instead, we need to step through the sequence and only adding items we have not seen before. We therefor
;	need to keep track of previously seen items so we can compare. If we add items as keys to a set we can
;	use 'contains?' for the comparison.

;	This solution returns a higher-order function to do the job

(defn my_distinct [coll]
  ((fn step [[f & rst] seen]
     (when f
       (if (seen f)
         (step rst seen)
         (cons f (step rst (conj seen f))))))
   coll #{}))

(= (my_distinct [1 2 1 3 1 2 4]) [1 2 3 4])
(= (my_distinct [:a :a :b :b :c :c]) [:a :b :c])
(= (my_distinct '([2 4] [1 2] [1 3] [1 3])) '([2 4] [1 2] [1 3]))
(= (my_distinct (range 50)) (range 50))

; 57. Simple Recursion

(= [5 4 3 2 1] ((fn foo [x] (when (> x 0) (conj (foo (dec x)) x))) 5))

; 58. Function Composition - without using comp

; Options:
;	Use higher order functions to next each function in turn using recursion.
;	Use reduce and apply to actually apply each function in turn (after reversing the order)

(defn my_comp [& fns]
    (fn [& args]
      (let [[f & fns] (reverse fns)]
        (reduce #(%2 %1) (apply f args) fns))))

(= [3 2 1] ((my_comp rest reverse) [1 2 3 4]))
(= 5 ((my_comp (partial + 3) second) [1 2 3 4]))
(= true ((my_comp zero? #(mod % 8) +) 3 5 7 9))
(= "HELLO" ((my_comp #(.toUpperCase %) #(apply str %) take) 5 "hello world"))

; 59. Juxtaposition - without using juxt

(defn my_juxt [x & xs]
  (fn [& args]
    (map #(apply % args) (cons x xs))))

(= [21 6 1] ((my_juxt + max min) 2 3 5 1 6 4))
(= ["HELLO" 5] ((my_juxt #(.toUpperCase %) count) "hello"))
(= [2 6 4] ((my_juxt :a :c :b) {:a 2, :b 4, :c 6, :d 8 :e 10}))

; 60. Sequence Reductions - without using reductions

(defn my_reductions
  ([f coll] (my_reductions f (first coll) (rest coll)))
  ([f init coll]
     (cons init
        (lazy-seq
            (when-let [s (seq coll)]
              (my_reductions f (f init (first s)) (rest s)))))))

(= (take 5 (my_reductions + (range))) [0 1 3 6 10])
(= (my_reductions conj [1] [2 3 4]) [[1] [1 2] [1 2 3] [1 2 3 4]])
(= (last (my_reductions * 2 [3 4 5])) (reduce * 2 [3 4 5]) 120)

; 61. Map Construction - without using zipmap

(= (#(into {} (map vector %1 %2)) [:a :b :c] [1 2 3]) {:a 1, :b 2, :c 3})
(= (#(into {} (map vector %1 %2)) [1 2 3 4] ["one" "two" "three"]) {1 "one", 2 "two", 3 "three"})
(= (#(into {} (map vector %1 %2)) [:foo :bar] ["foo" "bar" "baz"]) {:foo "foo", :bar "bar"})

; 62. Re-implement Iterate - with iterate!

(defn my_iterate [f x]
  (cons x (lazy-seq (my_iterate f (f x)))))

(= (take 5 (my_iterate #(* 2 %) 1)) [1 2 4 8 16])
(= (take 100 (my_iterate inc 0)) (take 100 (range)))
(= (take 9 (my_iterate #(inc (mod % 3)) 1)) (take 9 (cycle [1 2 3])))

; 63. Group a Sequence - without using group-by

(defn my_group-by [f xs] (apply merge-with concat (for [x xs] {(f x) [x]})))

(= (my_group-by #(> % 5) [1 3 6 8]) {false [1 3], true [6 8]})
(= (my_group-by #(apply / %) [[1 2] [2 4] [4 6] [3 6]])
   {1/2 [[1 2] [2 4] [3 6]], 2/3 [[4 6]]})
(= (my_group-by count [[1] [1 2] [3] [1 2 3] [2 3]])
   {1 [[1] [3]], 2 [[1 2] [2 3]], 3 [[1 2 3]]})

; 64. Intro to Reduce

(= 15 (reduce + [1 2 3 4 5]))
(=  0 (reduce + []))
(=  6 (reduce + 1 [2 3]))

; 65. Black Box Testing - with tons of restrictions

; Instead, we need to find out their base structures. Simplest way is to empty them and then
; compare - the clue is given in the last test example as to their base identities.

(defn coll-type [coll]
  (cond
  	(= (empty coll) #{}) :set
    (= (empty coll) {})  :map
    (= (empty coll) '()) (if (reversible? coll) :vector :list)))

(= :map (coll-type {:a 1, :b 2}))
(= :list (coll-type (range (rand-int 20))))
(= :vector (coll-type [1 2 3 4 5 6]))
(= :set (coll-type #{10 (rand-int 5)}))
(= [:map :set :vector :list] (map coll-type [{} #{} [] ()]))

; 66. Greatest Common Divisor

; Find the factors of each number as a set, find the intersection of the sets and take the max

(defn gcf [x y]
  (let [f (fn [z] (set (filter #(zero? (mod z %)) (range 1 (inc z))))) ; The 'factors function'
        xf (f x)  ; factors of x
        yf (f y)] ; factors of y
    (apply max (clojure.set/intersection xf yf))))

(= (gcf 2 4) 2)
(= (gcf 10 5) 5)
(= (gcf 5 7) 1)
(= (gcf 1023 858) 33)

; 67. Prime Numbers

; Method: See commented code below for clear version creating using functional decomposition

; (defn factors [x]
;  (filter #(zero? (mod x %)) (range 2 x)))
;
; (defn isprime? [z]
;  (empty? (factors z)))
;
; (defn prime-seq [n] (take n (filter #(isprime? %) (iterate inc 2)))) ; Uses an infinite lazy sequence

; Final, but obsfucated, code written as a sungle function

(defn prime-seq [n]
  (take n (filter (fn [x] (empty? ((fn factors [x] (filter #(zero? (mod x %)) (range 2 x))) x))) (iterate inc 2))))

(= (prime-seq 2) [2 3])
(= (prime-seq 5) [2 3 5 7 11])
(= (last (prime-seq 100)) 541)

; 68. Recurring Theme

(= [7 6 5 4 3]
  (loop [x 5
         result []]
    (if (> x 0)
      (recur (dec x) (conj result (+ 2 x)))
      result)))

; 69. Merge with a Function - without using merge-with

(defn my-merge-with [f & m]
  (let [unique-keys (distinct (flatten (map keys m)))
        values (map (fn [k] (reduce f (filter identity (map #(get % k) m)))) unique-keys)]
    (zipmap unique-keys values)))

(= (my-merge-with * {:a 2, :b 3, :c 4} {:a 2} {:b 2} {:c 5})
   {:a 4, :b 6, :c 20})
(= (my-merge-with - {1 10, 2 20} {1 3, 2 10, 3 15})
   {1 7, 2 10, 3 15})
(= (my-merge-with concat {:a [3], :b [6]} {:a [4 5], :c [8 9]} {:b [7]})
   {:a [3 4 5], :b [6 7], :c [8 9]})

; 70. Word Sorting

(defn sort-text [text] (sort-by #(clojure.string/lower-case %) (re-seq #"[a-zA-z]+" text)))

(= (sort-text  "Have a nice day.")
   ["a" "day" "Have" "nice"])
(= (sort-text  "Clojure is a fun language!")
   ["a" "Clojure" "fun" "is" "language"])
(= (sort-text  "Fools fall for foolish follies.")
   ["fall" "follies" "foolish" "Fools" "for"])

; 71. Rearranging Code: ->

(= (last (sort (rest (reverse [2 5 4 1 3 6]))))
   (-> [2 5 4 1 3 6] reverse rest sort __)
   5)

; 72. Rearranging Code: ->>

(= (apply + (map inc (take 3 (drop 2 [2 5 4 1 3 6]))))
   (->> [2 5 4 1 3 6] (drop 2) (take 3) (map inc) (__))
   11)

; 73. Analyze a Tic-Tac-Toe Board

; Method:
;  Flatten the structure then use 'map' to take each entry in all the winning positions, then repartition in groups of 3
;     - The winning positions are now horizonal [0 1 2] [3 4 5] [6 7 8]
;                                      vertical [0 3 6] [1 4 7] [2 5 8]
;                                  and diagonal [0 4 8] [2 4 6]
; Finally search the resulting vectors for 3 of either player's pieces - 'some' with a map is great for pattern matching

(defn chess [board]
  (some {[:o :o :o] :o [:x :x :x] :x}
        (partition 3 (map (vec (flatten board)) [0 1 2 3 4 5 6 7 8 0 3 6 1 4 7 2 5 8 0 4 8 2 4 6]))))

(= nil (chess [[:e :e :e]
            [:e :e :e]
            [:e :e :e]]))

(= :x (chess [[:x :e :o]
           [:x :e :e]
           [:x :e :o]]))

(= :o (chess [[:e :x :e]
           [:o :o :o]
           [:x :e :x]]))

(= nil (chess [[:x :e :o]
            [:x :x :e]
            [:o :x :o]]))

(= :x (chess [[:x :e :e]
           [:o :x :e]
           [:o :e :x]]))

(= :o (chess [[:x :e :o]
           [:x :o :e]
           [:o :e :x]]))

(= nil (chess [[:x :o :x]
            [:x :o :x]
            [:o :x :o]]))


; 150. Palindromic Numbers

; This works, but takes too long. Instead, it requires a more efficient way of producing palendromic numbers

(defn palendrome? [x] (filter #(= (str % ) (apply str (reverse (str %)))) (iterate inc x)))

(= (take 26 (palendrome? 0))
   [0 1 2 3 4 5 6 7 8 9
    11 22 33 44 55 66 77 88 99
    101 111 121 131 141 151 161])

(= (take 16 (palendrome? 162))
   [171 181 191 202
    212 222 232 242
    252 262 272 282
    292 303 313 323])

(= (take 6 (palendrome? 1234550000))
   [1234554321 1234664321 1234774321
    1234884321 1234994321 1235005321])

(= (first (palendrome? (* 111111111 111111111)))
   (* 111111111 111111111))

(= (set (take 199 (palendrome? 0)))
   (set (map #(first (palendrome? %)) (range 0 10000))))

(= true
   (apply < (take 6666 (palendrome? 9999999))))

(= (nth (palendrome? 0) 10101)
   9102019)
