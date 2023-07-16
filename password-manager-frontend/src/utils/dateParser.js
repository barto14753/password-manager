import moment from "moment";

export const milisecondsToDate = (milis) => {
	return moment(milis).format("yyyy-MM-DD HH:mm:ss");
};
