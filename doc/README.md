
# Table of Contents

1.  [Definition](#orga91db81)
2.  [Installation](#org08444b4)
3.  [Usage example](#org0894ce0)
4.  [Video tutorial](#org51bd7be)
5.  [License](#orgd7a9437)


<a id="orga91db81"></a>

# Definition

Makes working with pagination APIs fast. Based on core.async.


<a id="org08444b4"></a>

# Installation

![img](https://clojars.org/org.danielsz/blat/latest-version.svg)


<a id="org0894ce0"></a>

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


<a id="org51bd7be"></a>

# Video tutorial

Please [refer](https://www.youtube.com/watch?v=1KRWfVhbBM8) to the miniseries available on Youtube, "Exploratory programming with the TMDb API". 


<a id="orgd7a9437"></a>

# License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php) (the same as Clojure) together with the [966.icu](https://996.icu/#/en_US) [license](https://github.com/996icu/996.ICU/blob/master/LICENSE).

[![img](https://img.shields.io/badge/link-996.icu-red.svg)](https://img.shields.io/badge/link-996.icu-red.svg)
