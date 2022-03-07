interface QueriesInterface{
    checkManager:(login:string, pass:string) => boolean,
    checkUser:(userId:string) => boolean,
    checkUserSub:(userId:string) => boolean,
    getAllInfoTrain: () => Array<Object>
}