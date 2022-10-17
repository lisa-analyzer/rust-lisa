enum Message {
    Move { x: i32, y: i32 },
}

fn main() {
    let m = Message::Move {x : 1, y : 2};
    if let Message::Move {x: a, y: b} = m2 {
        println!("{}", a);
        println!("{}", b);
    }
}