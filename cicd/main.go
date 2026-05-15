package main

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"log"
	"os/exec"
	"strings"
)

func main() {
	r := gin.Default()

	r.GET("/hook/update", func(c *gin.Context) {

		branchCmd := exec.Command("git", "rev-parse", "--abbrev-ref", "HEAD")
		branchCmd.Dir = "./"

		branchOut, err := branchCmd.Output()
		if err != nil {
			c.JSON(500, gin.H{
				"error":  "Failed to get current branch",
				"detail": err.Error(),
			})
			return
		}

		branch := strings.TrimSpace(string(branchOut))

		commands := [][]string{
			{"git", "fetch", "--all"},
			{"git", "reset", "--hard", "origin/" + branch},
			{"git", "clean", "-fd"},
			{"docker-compose", "up", "-d", "--build"},
		}

		var fullOutput strings.Builder

		for _, args := range commands {
			cmd := exec.Command(args[0], args[1:]...)
			cmd.Dir = "./"

			out, err := cmd.CombinedOutput()

			fullOutput.WriteString(
				fmt.Sprintf(
					"Command: %v\nOutput:\n%s\n\n",
					args,
					string(out),
				),
			)

			if err != nil {
				log.Printf("Step failed: %v\nOutput: %s", args, string(out))

				c.JSON(500, gin.H{
					"error":  fmt.Sprintf("Step failed: %s", strings.Join(args, " ")),
					"detail": err.Error(),
					"output": fullOutput.String(),
				})

				return
			}
		}

		log.Printf("Full sync and deployment success:\n%s", fullOutput.String())

		c.JSON(200, gin.H{
			"message": "Git sync and Docker deployment success",
			"branch":  branch,
			"output":  fullOutput.String(),
		})
	})

	err := r.Run(":9999")
	if err != nil {
		log.Fatal(err)
	}
}
