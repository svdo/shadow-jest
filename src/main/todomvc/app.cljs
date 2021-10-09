(ns todomvc.app
  (:require [uix.core.alpha :as uix]
            [uix.dom.alpha :as uix.dom]))

(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (into (subvec coll 0 pos) (subvec coll (inc pos))))

(defn app []
  (let [!input (uix/ref nil)
        !todos (uix/state ["Create REPL"])]
    [:div
     [:header.header
      [:h1 "todos"]
      [:input.new-todo {:type "text"
                        :placeholder "What needs to be done?"
                        :auto-focus true
                        :on-key-down (fn [ev]
                                       (when (= "Enter" (.-key ev))
                                         (let [s (.-value @!input)]
                                           (swap! !todos conj s)
                                           (set! (.-value @!input) ""))))
                        :ref !input}]]
     [:section.main
      [:span
       [:input.toggle-all {:type "checkbox" :read-only true}]
       [:label]]

      (->> @!todos
           (map-indexed (fn [idx s]
                          [:li
                           [:div.view
                            [:input.toggle {:type "checkbox"}]
                            [:label s]
                            [:button.destroy {:data-testid "destroy"
                                              :on-click
                                              (fn []
                                                (swap! !todos (partial vec-remove idx)))}]]]))
           (into [:ul.todo-list]))]
     [:footer.footer
      [:span.todo-count "1 item left"]
      [:ul.filters
       [:li [:a.selected {:cursor :pointer} "All"]]
       [:li [:a {:cursor "pointer"} "Selected"]]
       [:li [:a {:cursor "pointer"} "Completed"]]]]]))
