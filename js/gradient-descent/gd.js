
const df = x => 2 * x


// """  梯度下降法。給定起始點與目標函數的一階導函數，求在epochs次反覆運算中x的更新值
// :param x_start: x的起始點
// :param df: 目標函數的一階導函數
// :param epochs: 反覆運算週期
// :param lr: 學習率
// : return: x在每次反覆運算後的位置（包括起始點），長度為epochs + 1
// """ 
function GD(x_start, df, epochs, lr) {
    // init xs
    const xs = []
    // find next x

    xs[0] = x_start
    x = x_start
    for (let i = 0; i < epochs; i++) {
        x = x - df(x) * lr
        // put it into xs
        xs[i + 1] = x
    }
    return xs
}

// # Main
// # 起始權重
// x_start = 5    
// # 執行週期數
// epochs = 15 
// # 學習率   
// lr = 0.3   
// # 梯度下降法 
// x = GD(x_start, dfunc, epochs, lr=lr) 

x_start = 5
epochs = 15
lr = 0.3

let res = GD(x_start, df, epochs, lr)

console.log(res)




