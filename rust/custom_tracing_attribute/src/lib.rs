use proc_macro2::TokenStream;
use quote::quote;
use syn::{parse_macro_input, ItemFn};

#[proc_macro]
pub fn custom_debug(_attr: TokenStream, item: TokenStream) -> TokenStream {
    let input_fn = parse_macro_input!(item as ItemFn);
    let fn_name = &input_fn.sig.ident;
    let fn_inputs = &input_fn.sig.inputs;
    let fn_body = &input_fn.block;

    let expanded = quote! {
        fn #fn_name #fn_inputs -> std::fmt::Result {
            let args = format!("{:?}", (#fn_inputs));
            println!("Function '{}' called with args: {}", stringify!(#fn_name), args);

            let result = (|| #fn_body)();

            println!("Function '{}' returned: {:?}", stringify!(#fn_name), result);
            result
        }
    };

    TokenStream::from(expanded)
}
