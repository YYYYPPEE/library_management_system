const API_BASE_URL = 'http://localhost:8081/api';

// 主题管理
function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    setTheme(savedTheme);
}

function setTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
    updateThemeIcon(theme);
}

function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    setTheme(newTheme);
}

function updateThemeIcon(theme) {
    const themeBtn = document.getElementById('themeSwitchBtn');
    if (themeBtn) {
        themeBtn.innerHTML = theme === 'light' ? '🌙' : '☀️';
        themeBtn.title = theme === 'light' ? '切换到夜间模式' : '切换到日间模式';
    }
}

function request(url, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const defaultOptions = {
        headers,
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

// 检查登录状态，如果未登录则跳转到登录页
function checkAuth() {
    const userInfo = getUserInfo();
    if (!userInfo) {
        // 如果不是在登录或注册页面，则跳转到登录页
        const currentPath = window.location.pathname;
        if (!currentPath.includes('login.html') && !currentPath.includes('register.html')) {
            alert('请先登录');
            window.location.href = 'login.html';
        }
    }
}

// 渲染导航栏
function renderNav() {
    const navLinks = document.getElementById('navLinks');
    const userInfo = getUserInfo();
    const currentTheme = localStorage.getItem('theme') || 'light';
    const themeIcon = currentTheme === 'light' ? '🌙' : '☀️';
    
    if (userInfo) {
        const isAdmin = userInfo.role === 1;
        navLinks.innerHTML = `
            <li class="nav-item">
                <button id="themeSwitchBtn" class="theme-switch-btn" onclick="toggleTheme()" title="切换到${currentTheme === 'light' ? '夜间' : '日间'}模式">${themeIcon}</button>
            </li>
            <li class="nav-item">
                <span class="nav-link">欢迎, ${userInfo.realName}</span>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="books.html">图书列表</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="my-borrows.html">我的借阅</a>
            </li>
            ${isAdmin ? `
                <li class="nav-item">
                    <a class="nav-link" href="books.html">图书管理</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="statistics.html">数据统计</a>
                </li>
            ` : ''}
            <li class="nav-item">
                <a class="nav-link" href="#" onclick="handleLogout(event)">退出登录</a>
            </li>
        `;
    } else {
        navLinks.innerHTML = `
            <li class="nav-item">
                <button id="themeSwitchBtn" class="theme-switch-btn" onclick="toggleTheme()" title="切换到${currentTheme === 'light' ? '夜间' : '日间'}模式">${themeIcon}</button>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="books.html">图书列表</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="login.html">登录</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="register.html">注册</a>
            </li>
        `;
    }
}

// 处理退出登录
function handleLogout(event) {
    event.preventDefault();
    logout();
    alert('已退出登录');
    window.location.href = '../index.html';
}

// 获取当前页面的相对路径（用于导航）
function getBasePath() {
    const path = window.location.pathname;
    if (path.includes('/pages/')) {
        return '../';
    }
    return '';
}

// 页面加载时初始化主题
document.addEventListener('DOMContentLoaded', initTheme);
