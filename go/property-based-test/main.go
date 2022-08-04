package propertybasedtest

import (
	"bytes"
	"fmt"
	"math/rand"
	"reflect"
	"strconv"
	"strings"
)

type User struct {
	Id   int
	Name string
}

func (User) Generate(r *rand.Rand, size int) reflect.Value {
	const alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01233456789"

	var buffer bytes.Buffer
	for i := 0; i < size; i++ {
		index := rand.Intn(len(alphabet))
		buffer.WriteString(string(alphabet[index]))
	}
	u := User{
		Id:   rand.Int(),
		Name: buffer.String(),
	}

	fmt.Println("generated user: ", u)

	return reflect.ValueOf(u)
}

func encode(u *User) string {
	return fmt.Sprintf("%v|%v", u.Id, u.Name)
}

func decode(s string) (*User, error) {
	fields := strings.Split(s, "|")

	id, err := strconv.Atoi(fields[0])

	if err != nil {
		return nil, fmt.Errorf("failed to parse field Id: %v", err)
	}

	name := fields[1]

	u := &User{
		Id:   id,
		Name: name,
	}

	return u, nil
}
