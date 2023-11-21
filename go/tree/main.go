package main

import "fmt"

/*
Level order traversal for this tree should look like:
	•	100
	•	50, 200
	•	25, 75, 350
*/

type Node struct {
	Left  *Node
	Right *Node
	val   int
}

func newNode(val int, left *Node, right *Node) *Node {
	return &Node{val: val, Left: left, Right: right}
}

// func preOrderTraversal(r *Node, val int) *Node {
// 	if r == nil {
// 		return &Node{val: val}
// 	}
// 	p := r
// 	for p != nil {
// 		switch {
// 		case val < p.val:
// 			fmt.Println("left, meet val: ", val)
// 			if p.Left == nil {
// 				goto NEWNODE
// 			}
// 			p = p.Left
// 		case val > p.val:
// 			fmt.Println("Right, meet val: ", val)
// 			if p.Right == nil {
// 				goto NEWNODE
// 			}
// 			p = p.Right
// 		default:
// 			goto NEWNODE
// 		}
// 	}
// NEWNODE:
// 	p = &Node{val: val}
// 	return r
// }

// [25, 75, 100, 200, 350]
//         100
//      50       200
// 25  75         350

//    p
//   50
// 25  75

// p = 50
// p = 25
// p = nil

// [100, 50, 25]
func traversal(r *Node) {
	q := []*Node{r}
	for len(q) > 0 {
		n := q[0]
		q = q[1:]
		fmt.Println(n.val)
		if n.Left != nil {
			q = append(q, n.Left)
		}
		if n.Right != nil {
			q = append(q, n.Right)
		}
	}
}

func main() {
	t := newNode(
		100,
		newNode(
			50,
			newNode(
				25,
				nil,
				nil,
			),
			newNode(
				75,
				nil,
				nil,
			),
		),
		newNode(
			200,
			nil,
			newNode(350, nil, nil),
		))
	traversal(t)
}
