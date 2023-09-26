# config/initializers/rswag_ui_csp_monkeypatch.rb
# Monkeypatch https://github.com/rswag/rswag/blob/master/rswag-ui/lib/rswag/ui/middleware.rb
# due to issues described in https://github.com/rswag/rswag/pull/619/files
# Link: https://github.com/rswag/rswag/pull/619#issuecomment-1693780676
module Rswag
  module Ui
    class Middleware
      # ORIGINAL:
      # def csp
      #   <<~POLICY.gsub "\n", ' '
      #     default-src 'self';
      #     img-src 'self' data:;
      #     font-src 'self' https://fonts.gstatic.com;
      #     style-src 'self' 'unsafe-inline' https://fonts.googleapis.com;
      #     script-src 'self' 'unsafe-inline';
      #   POLICY
      # end

      def csp
        # set default-src with * to PASS csp
        # https://stackoverflow.com/a/69569483
        <<~POLICY.gsub "\n", ' '
          default-src 'self' *;
          img-src 'self' data: https://validator.swagger.io;
          font-src 'self' https://fonts.gstatic.com;
          style-src 'self' 'unsafe-inline' https://fonts.googleapis.com;
          script-src 'self' 'unsafe-inline';
        POLICY
      end
    end
  end
end
