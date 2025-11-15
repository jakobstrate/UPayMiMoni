package main

import (
	"context"
	"log"
	"net/http"
	"os"

	"upaymimoni-backend/internal/fcm"
	internal_http "upaymimoni-backend/internal/http"
)

func main() {
	ctx := context.Background()

	fcmSender, err := fcm.NewClient(ctx)

	if err != nil {
		log.Fatalf("Failed to launch FCM client: %v", err)
	}

	h := internal_http.NewHandlers(fcmSender)

	router := h.NewRouter()

	port := os.Getenv("PORT")
	if port == "" {
		port = "3000"
	}

	log.Printf("Server starting on port %s", port)
	if err := http.ListenAndServe(":"+port, router); err != nil {
		log.Fatalf("Server Failed to start: %v", err)
	}
}
