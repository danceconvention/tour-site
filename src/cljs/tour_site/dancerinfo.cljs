(ns tour-site.dancerinfo
  (:require [tour-site.appdb :as appdb]
            [tour-site.points :refer [calculate-points]]))

(defn dancer-info [userid]
  (let [records (filter #(= userid (% :participantId)) (appdb/tourinfo-data 10))]
    [:div
      [:h2 (:firstName (first records)) " " (:lastName (first records))]
      [:table.table.table-striped.lead {:style {:margin-top "40px"}}
       [:thead
        [:tr
         [:th "Event"]
         [:th "Competition"]
         [:th "Placement"]
         [:th "Points"]]]
       [:tbody
        (for [record records]
          ^{:key (:recordId record)}
          [:tr
           [:td (:eventName record)]
           [:td (:contestName record)]
           [:td (:rank record)]
           [:td (calculate-points (:rank record))]
           ])
        [:tr
         [:td [:strong "Total points"]]
         [:td]
         [:td]
         [:td [:strong (reduce + (map #(calculate-points (:rank %)) records))]]
         ]]]]
    ))
