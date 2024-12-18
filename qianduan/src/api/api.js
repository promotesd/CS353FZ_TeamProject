import request from "@/utils/request";

//系统相关
export const login = (data) => {
    return request.post('/login', data, {
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
        }, params: {role: data.role}
    })
}

export const logout = (data) => {
    return request.get('/logout')
}


export const register = (data) => {
    return request.post('/system/register', data)
}


export const captcha = () => {
    return request.get('/system/captcha')
}


export const curryUser = () => {
    return request.get('/system/curryUser')
}


export const verification = (data) => {
    return request.get('/system/verification', {
        params: data
    })
}










