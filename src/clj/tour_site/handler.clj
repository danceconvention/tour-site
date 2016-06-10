(ns tour-site.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [tour-site.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css
     (if (env :dev) "/css/site.css" "/css/site.min.css")
     (if (env :dev) "/bootstrap-3.3.6/css/bootstrap.min.css" "/bootstrap-3.3.6/css/bootstrap.css"))
   ])

(def loading-page
  (html5
    (head)
    [:body {:style "padding-top: 80px;"}
     mount-target
     (include-js "/js/app.js")
     (include-js
       (if (env :dev) "/bootstrap-3.3.6/js/bootstrap.min.js" "/bootstrap-3.3.6/js/bootstrap.js"))
     ]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
