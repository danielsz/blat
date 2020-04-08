
# Table of Contents

1.  [Blat](#org7b846e2)
    1.  [Definition](#org0661bd9)
    2.  [Installation](#org097e009)
    3.  [Usage example](#orgcce09d7)
        1.  [Example I](#org052192f)
        2.  [Example II](#org8e4afd1)
    4.  [Video tutorial](#org9eb2e75)
    5.  [License](#orgc626717)


<a id="org7b846e2"></a>

# Blat

In Yiddish, בלאַט means *page*.


<a id="org0661bd9"></a>

## Definition

Leverage pagination APIs with core.async.


<a id="org097e009"></a>

## Installation

![img](https://clojars.org/org.danielsz/blat/latest-version.svg)


<a id="orgcce09d7"></a>

## Usage example


<a id="org052192f"></a>

### Example I

Given a function that knows how to operate asynchronously on a page in a pagination API, for example, the tMDB API:

    (require '[clojure.core.async :as a])
    
    (defn movies [query page c]
      (client/get (str api-endpoint "/search/movie") {:query-params {"api_key" api-key
    								 "query" query
    								 "page" page}
    						  :async? true
    						  :as :json}
    	      (fn [resp] (a/onto-chan c (:results (:body resp))))
    	      (fn [e] (a/>!! c (.getMessage e))))

It is then possible to quickly operate on any number of pages. 

    (require ' [blet.core :refer [fetch]])
    (fetch find 1 50)

**Note:** `start` is inclusive, while `end` is exclusive, like Clojure's `range` function.


<a id="org8e4afd1"></a>

### Example II

Given a function that knows how to operate both synchronously and asynchronously on a page in a pagination API, for example, the tMDB API:

    (require '[clojure.core.async :as a])
    
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


<a id="org9eb2e75"></a>

## Video tutorial

Please [refer](https://www.youtube.com/watch?v=1KRWfVhbBM8) to the miniseries available on Youtube, "Exploratory programming with the TMDb API". 


<a id="orgc626717"></a>

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php) (the same as Clojure) together with the [966.icu](https://996.icu/#/en_US) [license](https://github.com/996icu/996.ICU/blob/master/LICENSE).

[![img](https://img.shields.io/badge/link-996.icu-red.svg)](https://img.shields.io/badge/link-996.icu-red.svg)

