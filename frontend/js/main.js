const API_BASE_URL = 'http://localhost:8081/api';

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

function saveToken(token) {
    localStorage.setItem('token', token);
}

function getToken() {
    return localStorage.getItem('token');
}

function saveUserInfo(userInfo) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
}

function getUserInfo() {
    const userInfo = localStorage.getItem('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
}
