package propertybasedtest

import (
	"testing"
	"testing/quick"
)

// Ref: https://code.egym.de/property-based-testing-in-golang-d3a860a2965
func TestEncodeDecode(t *testing.T) {
	symmetrical := func(u User) bool {
		roundTripUser, err := decode(encode(&u))
		return err == nil && *roundTripUser == u
	}

	if err := quick.Check(symmetrical, nil); err != nil {
		t.Errorf("symmetrical encode -> decode fail: %v", err)
	}
}
