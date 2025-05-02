import './Test.css'

export default function Test() {
    return (
        <div className="min-h-screen bg-gray-50 p-8">
            <div className="max-w-4xl mx-auto">
                <h1 className="text-3xl font-bold text-gray-800 mb-6">
                    Test Page
                </h1>
                <p className="text-gray-600">
                    This is a test page at /test endpoint
                </p>
								<div className="page-container">
									<div className="sidebar-container">
										<p>sidebar</p>
									</div>
									<div className="main-content-container">
										<ul>
											<li>Item 1</li>
											<li>Item 2</li>
											<li>Item 3</li>
											<li>Item 4</li>
											<li>Item 5</li>
											<li>Item 6</li>
											<li>Item 7</li>
											<li>Item 8</li>
											<li>Item 9</li>
											<li>Item 10</li>
										</ul>

									</div>
								</div>
            </div>
        </div>
    )
} 