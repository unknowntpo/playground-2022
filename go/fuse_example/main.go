package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"path/filepath"
	"strings"
	"syscall"

	"github.com/hanwen/go-fuse/v2/fs"
	"github.com/hanwen/go-fuse/v2/fuse"
)

// files contains the files we will expose as a file system
var files = map[string]string{
	"file":              "content",
	"subdir/other-file": "other-content",
}

// inMemoryFS is the root of the tree
type inMemoryFS struct {
	fs.Inode
}

// Ensure that we implement NodeOnAdder
var _ = (fs.NodeOnAdder)((*inMemoryFS)(nil))

// OnAdd is called on mounting the file system. Use it to populate
// the file system tree.
func (root *inMemoryFS) OnAdd(ctx context.Context) {
	for name, content := range files {
		dir, base := filepath.Split(name)

		p := &root.Inode

		// Add directories leading up to the file.
		for _, component := range strings.Split(dir, "/") {
			if len(component) == 0 {
				continue
			}
			ch := p.GetChild(component)
			if ch == nil {
				// Create a directory
				ch = p.NewPersistentInode(ctx, &fs.Inode{},
					fs.StableAttr{Mode: syscall.S_IFDIR})
				// Add it
				p.AddChild(component, ch, true)
			}

			p = ch
		}

		// Make a file out of the content bytes. This type
		// provides the open/read/flush methods.
		embedder := &fs.MemRegularFile{
			Data: []byte(content),
		}

		// Create the file. The Inode must be persistent,
		// because its life time is not under control of the
		// kernel.
		child := p.NewPersistentInode(ctx, embedder, fs.StableAttr{})

		// And add it
		p.AddChild(base, child, true)
	}
}

// This demonstrates how to build a file system in memory. The
// read/write logic for the file is provided by the MemRegularFile type.
func main() {
	// This is where we'll mount the FS
	mntDir, _ := os.MkdirTemp("", "")

	root := &inMemoryFS{}
	server, err := fs.Mount(mntDir, root, &fs.Options{
		MountOptions: fuse.MountOptions{Debug: false},
	})
	if err != nil {
		log.Panic(err)
	}

	log.Printf("Mounted on %s", mntDir)
	log.Printf("Unmount by calling 'fusermount -u %s'", mntDir)

	// Set up signal handling for Ctrl+C (SIGINT)
	signalChan := make(chan os.Signal, 1)
	signal.Notify(signalChan, os.Interrupt, syscall.SIGTERM)

	// Run a goroutine to handle the signal
	go func() {
		<-signalChan
		log.Println("Received interrupt signal, unmounting...")
		server.Unmount()
	}()

	// Wait until unmount before exiting
	server.Wait()
}
