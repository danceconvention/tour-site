(ns tour-site.top5
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]))

(def entries (reagent/atom nil))

(defn calculate-points [rank] (inc (- 10 rank)))

(defn retrieve-leaderboard [rank]
  (GET (str "http://localhost:8080/eventdirector/rest/v1/tour/rawcon/top?rank=" rank)
       :handler (fn [response] (reset! entries response)))
  )

(defn leaderboard [maxrank size]
    (retrieve-leaderboard maxrank)
    (fn []
      (let [entries-list  (map (fn[h] (into {} (for [[k v] h] [(keyword k) v]))) @entries)
            grouped-list  (group-by :participantId entries-list)
            summed-list   (map (fn[userId] {:participantId userId
                                            :fullName      (str (:firstName (first (grouped-list userId))) " " (:lastName (first(grouped-list userId))))
                                            :total         (reduce + (map calculate-points (map :rank (grouped-list userId))))
                                            }) (keys grouped-list))
            sorted-list   (take size (reverse (sort-by :total summed-list)))]
      [:table.table.table-striped.lead {:style {:margin-top "40px"}}
       [:tbody
       (for [entry sorted-list]
         ^{:key (:participantId entry)}
         [:tr
          [:td.col-md-11 (:fullName entry) ]
          [:td.col-md-1 (:total entry)]])]])))
