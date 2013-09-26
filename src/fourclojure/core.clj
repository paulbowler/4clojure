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


