
# Table of Contents

1.  [Blat](#org6520625)
    1.  [Definition](#org5926783)
    2.  [Installation](#orgad3d36b)
    3.  [Usage example](#org25fd880)
        1.  [Example I](#org35bd5a1)
        2.  [Example II](#orgd900f14)
    4.  [Video tutorial](#org30393bd)
    5.  [License](#orgcd56def)


<a id="org6520625"></a>

# Blat

In Yiddish, בלאַט means *page*.


<a id="org5926783"></a>

## Definition

Leverage pagination APIs with core.async.


<a id="orgad3d36b"></a>

## Installation

![img](https://clojars.org/org.danielsz/blat/latest-version.svg)


<a id="org25fd880"></a>

## Usage example


<a id="org35bd5a1"></a>

### Example I

Given a function that knows how to operate asynchronously on a page in a pagination API, for example, the tMDB API:

    (require '[clojure.core.async :as a]
    	 '[clj-http.client :as client])
    
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


<a id="orgd900f14"></a>

### Example II

Given a function that knows how to operate both synchronously and asynchronously on a page in a pagination API, for example, the tMDB API:

    (require '[clojure.core.async :as a]
    	 '[clj-http.client :as client])
    
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

You can then use the `blat` library to retrieve all pages concurrently without knowing beforehand how many pages of results are available, leveraging the API instead. 

    (require ' [blet.core :refer [fetch]])
    (defn fetch-all [query]
      (let [f (partial find query)
    	{results :results total :total_pages} (f 1)]
        (fetch f 2 (inc total) results)))

The first use of `find` is synchronous. It is called with two arguments, query and the first page of results. The results are destructured to get the number of total pages and the initial results. The second use of `find` is asynchronous via `fetch`, which internally calls it with an arity of 3, the third argument being a channel.


<a id="org30393bd"></a>

## Video tutorial

Please [refer](https://www.youtube.com/watch?v=1KRWfVhbBM8) to the miniseries available on Youtube, "Exploratory programming with the TMDb API". 


<a id="orgcd56def"></a>

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php) (the same as Clojure) together with the [966.icu](https://996.icu/#/en_US) [license](https://github.com/996icu/996.ICU/blob/master/LICENSE).

[![img](https://img.shields.io/badge/link-996.icu-red.svg)](https://img.shields.io/badge/link-996.icu-red.svg)

