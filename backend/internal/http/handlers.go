package http

import (
	"encoding/json"
	"log"
	"net/http"

	"upaymimoni-backend/internal/fcm"
)

type Handlers struct {
	FCMSender fcm.FCMSender
}

func NewHandlers(sender fcm.FCMSender) *Handlers {
	return &Handlers{
		FCMSender: sender,
	}
}

type NotificationBody struct {
	Title   string `json:"title"`
	Message string `json:"message"`
}

type NotificationRequest struct {
	RecipientToken   string           `json:"recipientToken"`
	NotificationBody NotificationBody `json:"notificationBody"`
}

func (h *Handlers) NewRouter() *http.ServeMux {
	mux := http.NewServeMux()
	mux.HandleFunc("/notifications/send", h.sendNotificationHandler)
	return mux
}

func (h *Handlers) sendNotificationHandler(w http.ResponseWriter, r *http.Request) {
	log.Printf("Recieved request for %s from %s", r.URL.Path, r.RemoteAddr)

	if r.Method != http.MethodPost {
		http.Error(w, "Http method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req NotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		log.Printf("Error decoding json body: %v", err)
		http.Error(w, "Invalid request format", http.StatusBadRequest)
		return
	}

	title := req.NotificationBody.Title
	body := req.NotificationBody.Message

	if req.RecipientToken == "" || title == "" || body == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	err := h.FCMSender.SendMessage(r.Context(), req.RecipientToken, title, body)
	if err != nil {
		log.Printf("FCM send failed for token %s: %v", req.RecipientToken, err)
		http.Error(w, "Failed to send notification", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "Notification sent Succesfully"}`))
}
