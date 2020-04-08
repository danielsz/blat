
# Table of Contents

1.  [Definition](#org53ea7e2)
2.  [Installation](#org2a10038)
3.  [Usage example](#orgf5d256a)
4.  [Video tutorial](#orgc1e82f5)
5.  [License](#orgad3cf40)


<a id="org53ea7e2"></a>

# Definition

Leverage pagination APIs with core.async.


<a id="org2a10038"></a>

# Installation

![img](https://clojars.org/org.danielsz/blat/latest-version.svg)


<a id="orgf5d256a"></a>

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

The first use of `find` is synchronous. It is called with two arguments, query and the first page of results. The results are destructured to get the number of total pages and the initial results. The second use of `find` is asynchronous via `fetch`, which internally calls it with an arity of 3, the third argument being a channel.


<a id="orgc1e82f5"></a>

# Video tutorial

Please [refer](https://www.youtube.com/watch?v=1KRWfVhbBM8) to the miniseries available on Youtube, "Exploratory programming with the TMDb API". 


<a id="orgad3cf40"></a>

# License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php) (the same as Clojure) together with the [966.icu](https://996.icu/#/en_US) [license](https://github.com/996icu/996.ICU/blob/master/LICENSE).

[![img](https://img.shields.io/badge/link-996.icu-red.svg)](https://img.shields.io/badge/link-996.icu-red.svg)

