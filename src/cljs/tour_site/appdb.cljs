(ns tour-site.appdb
  (:require [ajax.core :refer [GET]]
            [reagent.core :as reagent]))

(def ^:private app-db (reagent/atom {}))

(defn- convert-maps [jsondata] (map (fn[h] (into {} (for [[k v] h] [(keyword k) v]))) jsondata))

(defn- retrieve-tourinfo[rank]
  (GET (str "http://localhost:8080/eventdirector/rest/v1/tour/rawcon/top?rank=" rank)
       :handler (fn [response] (swap! app-db assoc :tourinfo (convert-maps response)))))

(defn tourinfo-data [rank]
  (if-not (contains? @app-db :tourinfo)
    (retrieve-tourinfo rank))
  (:tourinfo @app-db))

