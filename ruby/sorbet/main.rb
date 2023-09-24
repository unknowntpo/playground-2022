require 'sorbet-runtime'

module Log
  # typed: true
  extend T::Sig

  sig { params(env: T::Hash[Symbol, Integer], key: Symbol).void }
  def log_env(env, key)
    puts "LOG: #{key} => #{env[key]}"
  end
end

Log.log_env({ timeout_len: 2000 }, 'timeout_len') # => Expected `Symbol` but found `String("timeout_len")`