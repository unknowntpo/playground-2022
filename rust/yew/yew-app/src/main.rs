use yew::{function_component, html, Html};

#[function_component(App)]
fn app() -> Html {
    html! {
        <h1>{ "Hello World" }</h1>
    }
}

fn main() {
    println!("Hello, world!");
    yew::Renderer::<App>::new().render();
}
