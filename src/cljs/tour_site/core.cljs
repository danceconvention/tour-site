(ns tour-site.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [goog.string :as gstring]
              [cljsjs.jquery]
              [tour-site.top5 :as top5]
              [tour-site.search :as search]
              [tour-site.dancerinfo :as dancerinfo]))

;; -------------------------
;; Views

(defn- tour-info []
  [:div.well.well-sm
   [:p [:span "The Raw Con Comp Tour is designed to encourage and reward dancers of all levels to compete at Raw Con Events. The spirit of the Tour is to foster personal growth whilst competing in a friendly manner."]]
   [:p [:span "Competitors will be rewarded points for all competitions (J&J, Strictly, Classic, Masters, Pro/Am) from 1st to 10th placings"]]
   [:p [:span "The male and female with the most points at the end of the Tour will win a return flight to the USA and entry to Halloween Swing Thing on the last weekend in October 2017!"]]])

(defn site-layout [contents]
  [:div
   [:nav.navbar.navbar-inverse.navbar-fixed-top
    [:div.container
     [:div.navbar-header
      [:a.navbar-brand {:href "https://danceconvention.net/"}
       [:nobr
       [:img {:src "/img/dcnet_logo_header.png" :border "0" :alt "danceConvention.net"}]
       (gstring/unescapeEntities "&nbsp;")
       [:strong "danceConvention.net"]]]
      ]
     [:div#navbar.navbar-collapse.collapse
      [:ul.nav.navbar-nav.navbar-right
       [:li [:a.navbar-brand {:href "/"} [:strong "Raw Connection Competition Tour"]]]]]]]
   [:div.container
    [:div.row
     [:div.col-md-6 [:a.thumbnail {:href "/"} [:img {:src "/img/rawcontour.jpg" :border "0" :alt "Raw Connection Competition Tour"}]]]
     [:div.col-md-6 [tour-info]]]
    [:div.row
     [:div.col-md-12 contents]]
    [:footer {:style {:margin-top "40px"}} [:p [:small "Powered by danceConvention.net " (gstring/unescapeEntities "&copy;") " 2013-2016 Ilya Obshadko"]]]]
  ]
)

(defn home-page []
  (site-layout [:div.row
                [:div.col-md-6 [:h2 [:small "Leaderboard"]] [top5/leaderboard 10 10]]
                [:div.col-md-6
                 [:h2 [:small "Competitor search"] ]
                 [search/search-form]
                 [:h2 [:small "Links"]]
                 [:ul.list-unstyled {:style {:font-size "130%"}}
                  [:li [:a {:href "https://www.facebook.com/groups/1142947162412936/" :target "_blank"} (gstring/unescapeEntities "&raquo;") " Read our Facebook group"]]
                  [:li [:a {:href "http://www.rawconnection.com.au/" :target "_blank"} (gstring/unescapeEntities "&raquo;") " Visit Raw Connection website"]]
                  [:li [:a {:href "https://danceconvention.net/eventdirector/en/eventpage/471220" :target "_blank"} (gstring/unescapeEntities "&raquo;") " Register for Swingtimate 2016"]]
                  [:li [:a {:href "https://danceconvention.net/eventdirector/en/eventpage/541940" :target "_blank"} (gstring/unescapeEntities "&raquo;") " Register for Australasian 2017"]]]]
                ]))

(defn user-page []
  (site-layout [dancerinfo/dancer-info (session/get :current-user)]))

(defn about-page []
  (site-layout [:div [:h2 "About tour-site"]
               [:div [:a {:href "/"} "go to the home page"]]]))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/dancerinfo/:userid" [userid]
  (session/put! :current-user (js/parseInt userid))
  (session/put! :current-page #'user-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
