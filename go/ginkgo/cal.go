package cal

type Calculator struct{}

func (c Calculator) Add(x, y int) int {
	return x + y
}

func NewCal() Calculator {
	return Calculator{}
}
