export const parseJwt = (jwt) => {
  try {
    return JSON.parse(atob(jwt.split('.')[1]))
  } catch (e) {
    console.log(e)
  }
}

export const isUser = (user) => (user ? parseJwt(user.access_token) : false)