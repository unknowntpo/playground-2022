import "bootstrap"
import "bootstrap/dist/css/bootstrap.min.css"

const queryParamsContainer = document.querySelector('[data-query-params]')
const requestHeadersContainer = document.querySelector('query-request-headers]')

queryParamsContainer.append(createKeyValuePair())
requestHeadersContainer.append(createKeyValuePair())

function createKeyValuePair() {
    const element = keyValueTemplate.textContent.cloneNode(true)
    element.querySelector('[data-remove-btn]').addEventListener('click', (e) => {
        e.target.closest("[data-key-value-pair]").remove()
    })
    return element
}
