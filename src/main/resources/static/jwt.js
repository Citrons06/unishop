document.addEventListener('DOMContentLoaded', function () {
    function fetchWithAuth(url, options = {}) {
        const headers = new Headers(options.headers || {});
        const token = localStorage.getItem('accessToken');
        if (token) {
            headers.append('Authorization', 'Bearer ' + token);
            console.log('Token attached to header:', token);
        } else {
            console.log('No token found in localStorage');
        }
        options.headers = headers;
        return fetch(url, options);
    }

    window.fetchWithAuth = fetchWithAuth;

    if (document.getElementById('user-info')) {
        fetchWithAuth('/api/user/info')
            .then(response => response.json())
            .then(data => {
                const userInfoDiv = document.getElementById('user-info');
                if (userInfoDiv) {
                    userInfoDiv.innerHTML = `<p>사용자: ${data.username}</p>`;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                const userInfoDiv = document.getElementById('user-info');
                if (userInfoDiv) {
                    userInfoDiv.innerHTML = `<p>사용자 정보를 가져오지 못했습니다.</p>`;
                }
            });
    }

    window.logout = function() {
        const refreshToken = localStorage.getItem('refreshToken');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');

        fetchWithAuth('/api/user/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ refreshToken: refreshToken })
        }).then(response => {
            if (response.ok) {
                window.location.href = '/';
            } else {
                console.error('Logout failed');
            }
        });
    };
});