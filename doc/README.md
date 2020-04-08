
# Table of Contents

1.  [Definition](#orga9942b5)
2.  [Installation](#org33759f1)
3.  [Usage example](#orgd5c8906)
4.  [Video tutorial](#org4164209)
5.  [License](#org86b1474)


<a id="orga9942b5"></a>

# Definition

Leverage pagination APIs with core.async.


<a id="org33759f1"></a>

# Installation

![img](https://clojars.org/org.danielsz/blat/latest-version.svg)


<a id="orgd5c8906"></a>

# Usage example

Given a function that knows how to operate on a page in a pagination API, for example, the tMDB API:

    (defn movies
      ([query page]
       (-> (client/get (str api-endpoint "/search/movie") {:query-params {"api_key" api-key
    								     "query" query
    								     "page" page}
    						      :as :json})
          :body))
      ([query page c]
      (client/get (str api-endpoint "/search/movie") {:query-params {"api_key" api-key
    								 "query" query
    								 "page" page}
    						  :async? true
    						  :as :json}
    	      (fn [resp] (a/onto-chan c (:results (:body resp))))
    	      (fn [e] (a/>!! c (.getMessage e))))))

You can then use the `blat` library to retrieve all pages concurrently. 

    (require ' [blet.core :refer [fetch]])
    (defn fetch-all [query]
      (let [f (partial find query)
    	{results :results total :total_pages} (f 1)]
        (fetch f 2 (inc total) results)))

The first use of `find` is synchronous. It is called with two arguments, query and the first page of results. The results are destructured to get the the number of total pages and the initial results. The second use of `find` is asynchronous via `fetch`, which internally calls it with an arity of 3, the third argument being a channel.


<a id="org4164209"></a>

# Video tutorial

Please [refer](https://www.youtube.com/watch?v=1KRWfVhbBM8) to the miniseries available on Youtube, "Exploratory programming with the TMDb API". 


<a id="org86b1474"></a>

# License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php) (the same as Clojure) together with the [966.icu](https://996.icu/#/en_US) [license](https://github.com/996icu/996.ICU/blob/master/LICENSE).

[![img](https://img.shields.io/badge/link-996.icu-red.svg)](https://img.shields.io/badge/link-996.icu-red.svg)

