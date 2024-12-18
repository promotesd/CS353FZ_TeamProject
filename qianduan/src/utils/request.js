import axios from "axios";
import {ElMessage, ElNotification} from 'element-plus'
import router from "@/router";
import {useUserStore} from "@/stores/userInfo";

const request = axios.create({
    baseURL: '/api',
    withCredentials: true
})


// 添加响应拦截器
request.interceptors.response.use(
    function (response) {
        // 对响应数据进行操作
        let code = response.data.code
        let showMsg = response.data.showMsg
        if (code === 200&&showMsg===true) {
            ElMessage.success(response.data.msg)
        } else if (code === 403) {
            ElNotification({
                title: 'Error',
                message: response.data.msg,
                type: 'error',
                duration: 800,
                onClose:()=>{
                    router.push({path: '/'})
                }
            })
        }
        else if(code>200){
            ElNotification({
                title: 'Error',
                message: response.data.msg,
                type: 'error',
                duration: 800
            })
        }

        if (response.headers.access_token){
            const userStore=useUserStore()
            userStore.user.token=response.headers.access_token
        }

        return response
    },
    function (error) {
        // 对响应错误进行操作
        ElNotification({
            title: 'Error',
            message: error,
            type: 'error',
            duration: 800
        })
        return Promise.reject(error);
    }
);

request.interceptors.request.use((config)=>{
    const userStore=useUserStore()
    if (userStore.user){
        config.headers.Authorization=userStore.user.token
    }
    return config
},(error)=>{
    return Promise.reject(error)
})


export default request;
