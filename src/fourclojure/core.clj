;;
;; 4clojure.com Solutions, 2012 Paul Bowler
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