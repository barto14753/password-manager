import axios from 'axios'


export function apiPost(url, body) {
  const config = {
    headers: {
      'Content-Type': 'application/json'
    }
  };
  console.log(body);
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