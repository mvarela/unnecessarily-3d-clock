(ns fi.varela.clock.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [fi.varela.clock.events :as events]
   [fi.varela.clock.views :as views]
   [fi.varela.clock.config :as config]
   [threeagent.core :as t]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app"))
  (t/render [views/root]
             (.getElementById js/document "root")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (js/setInterval (fn[] (re-frame/dispatch-sync [::events/time])) 100)
  (dev-setup)
  (mount-root))
