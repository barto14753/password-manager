import axios from 'axios'


export function apiPost(url, body) {
  const config = {
    headers: {
      'Content-Type': 'application/json'
    }
  };
  return axios.post(url, JSON.stringify(body), config)
      .then((response) => response.data)
      .catch((error) => {
        throw error
      })
  }
  
export function apiGet(url, params) {
    return axios.get(url, {
      params: params
    })
      .then((response) => response.data)
      .catch((error) => {
        throw error
      })
  }

export function apiPatch(url, body) {
    const accessToken = sessionStorage.getItem('access_token');
    const headers = {
      Authorization: `Bearer ${accessToken}`,
    };

    return axios.patch(url, body, 
      {
      withCredentials: true,
      headers: headers,
    })
      .then((response) => response.data)
      .catch((error) => {
        throw error
      })
  }