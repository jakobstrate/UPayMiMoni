package fcm

import (
	"context"
	"fmt"
	"log"
	"os"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
	"google.golang.org/api/option"
)

type FCMSender interface {
	SendMessage(ctx context.Context, token, title, body string) error
}

type Client struct {
	fcmClient *messaging.Client
}

func NewClient(ctx context.Context) (FCMSender, error) {
	credPath := os.Getenv("GOOGLE_APPLICATION_CREDENTIALS")

	if credPath == "" {
		return nil, fmt.Errorf("GOOGLE_APPLICATION_CREDENTIALS not set: %s", credPath)
	}
	opt := option.WithCredentialsFile(credPath)
	app, err := firebase.NewApp(ctx, nil, opt)
	if err != nil {
		return nil, fmt.Errorf("error initializing firebase app: %w", err)
	}

	client, err := app.Messaging(ctx)
	if err != nil {
		return nil, fmt.Errorf("error getting messaging clientL %w", err)
	}

	log.Println("Firebase messaging client started succesfully")

	return &Client{fcmClient: client}, nil
}

func (c *Client) SendMessage(ctx context.Context, token, title, body string) error {
	message := &messaging.Message{
		Token: token,
		Data: map[string]string{
			"title": title,
			"body": body,
		},
	}

	response, err := c.fcmClient.Send(ctx, message)
	if err != nil {
		return fmt.Errorf("error sending message: %s", response)
	}

	return nil
}
