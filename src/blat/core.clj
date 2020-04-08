(ns blat.core
  (:require
   [clojure.core.async :as a]))

(defn find-async
  "`f` is a function of two arguments. The first argument is a page
  number. The second argument is a channel on which to write the page
  results. f should return immediately. If it does any kind of i/o,
  use an asynchronous library. If it needs to compute, make it asynchronous.
  `start` is the first page.
  `end` is the last page.
  `coll` is the initial collection of results. Defaults to an empty
  vector.
  `xf` is a transducer that will be applied when reading the channel
  that was passed to `f`. Defaults to a no-op."
  ([f start end]
   (find-async f start end []))
  ([f start end coll]
   (find-async f start end coll (map identity)))
  ([f start end coll xf]
   (let [g (fn [page]
             (let [c (a/chan 1 xf)]
               (f page c)
               c))
         channels (map g (range start end))]
     (->> channels
        a/merge
        (a/reduce conj coll)
        a/<!!))))


