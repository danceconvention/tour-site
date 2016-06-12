(ns tour-site.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [tour-site.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app.container
      [:h3 "Loading Competition Tour..."]
      [:p "If this message doesn't disappear, please enable JavaScript in your browser."]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css
     (if (env :dev) "/css/site.css" "/css/site.min.css")
     (if (env :dev) "/bootstrap-3.3.6/css/bootstrap.css" "/bootstrap-3.3.6/css/bootstrap.min.css"))
   ])

(def loading-page
  (html5
    (head)
    [:body {:style "padding-top: 80px;"}
     mount-target
     (include-js "/js/app.js")
     (include-js
       (if (env :dev) "/bootstrap-3.3.6/js/bootstrap.js" "/bootstrap-3.3.6/js/bootstrap.min.js"))
     ]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/dancerinfo/:userid" [userid] loading-page)
  (GET "/about" [] loading-page)
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
