const API_BASE_URL = 'http://localhost:8080/api';

function request(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
        ...options
    };
    return fetch(`${API_BASE_URL}${url}`, defaultOptions)
        .then(response => response.json());
}
