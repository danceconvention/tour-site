(ns tour-site.top5
  (:require [tour-site.appdb :as appdb]
            [tour-site.points :refer [calculate-points]]))

(defn leaderboard [maxrank size]
    (fn []
      (let [entries-list  (appdb/tourinfo-data maxrank)
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
          [:td.col-md-11 [:a {:href (str "/dancerinfo/" (:participantId entry))} (:fullName entry)]]
          [:td.col-md-1 (:total entry)]])]])))
