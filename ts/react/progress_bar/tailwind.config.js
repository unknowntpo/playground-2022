module.exports = {
	// ... other config
	theme: {
		extend: {
			keyframes: {
				'scale-in': {
					'0%': { transform: 'scale(0)' },
					'100%': { transform: 'scale(1)' }
				}
			},
			animation: {
				'scale-in': 'scale-in 0.2s ease-out'
			}
		}
	}
} 