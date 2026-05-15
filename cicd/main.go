package main

import (
	"log"
	"os/exec"

	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()

	r.GET("/hook/update", func(c *gin.Context) {
		cmd := exec.Command("git", "pull")
		cmd.Dir = "./"

		out, err := cmd.CombinedOutput()
		if err != nil {
			log.Printf("Git pull: %s\n: %s", err, string(out))
			c.JSON(500, gin.H{"error": err.Error(), "output": string(out)})
			return
		}

		log.Printf("Git pull: %s", string(out))
		c.JSON(200, gin.H{"message": "Success", "output": string(out)})
	})

	err := r.Run()
	if err != nil {
		return
	}
}
